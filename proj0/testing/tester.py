#!/usr/bin/env python3
# -*-Python-*-

from testing import Tester, contents, interpret_problem
import os, sys, re
import io
import getopt
from os.path import join

PROGRAM = "java -ea signpost.Main --testing --no-display"

os.environ.pop('DISPLAY', None)

def compare_loose(received, expected_pattern):
    """Compares user output RECEIVED to expected output EXPECTED_PATTERN,
       but allows EXPECTED_PATTERN  to contain boards that begin '*B[' or '**B['
       rather than 'B['.  These boards represent places where the tested
       board is generated randomly.  Rather than a literal comparison, for
       a board in expected_pattern that begins with '*B[':
            1. Arrow directions on the tested board are ignored, except that
               the highest-numbered square must have "*" as its direction.
            2. Numbers on the tested board are compared only when the
               corresponding number on the standard board is not blank.
            3. Connection status on the tested board is ignored.
       For a board in expected_pattern that begins with '**B[':      
            1. The blank numbers, arrow directions, and connection status
               indications on the standard board are ignored.
            2. The tested board is checked to make sure it is a valid
               puzzle solution.
       In other words, the standard board is arbitrary and used only to check
       size.  Thus, it may safely be generated by a reference program and used
       verbatim.

       Returns either True (if OK) or an error message string."""
    
    actual_boards = re.findall(r'(?ms)^B\[.*?\]', received)
    expected_boards = re.findall(r'(?ms)^\*?\*?B\[.*?\]', expected_pattern)
    if len(actual_boards) != len(expected_boards):
        return "There are {} output boards; expected {}."\
               .format(len(actual_boards), len(expected_boards))
    for cnt, (actual_board, std_board) in \
        enumerate(zip(actual_boards, expected_boards)):

        cnt += 1

        if std_board[0] != '*':
            if actual_board == std_board:
                continue
            else:
                return "Output board #{} does not match standard.".format(cnt)
        actual = re.split(r'\r?\n', actual_board)
        std = re.split(r'\r?\n', std_board)
        if len(actual) != len(std):
            return "Height of output board #{} does not match standard."\
                   .format(cnt)
        del actual[-1]
        del std[-1]
        if re.sub(r'.*B\[', '', actual[0]) != re.sub(r'.*B\[', '', std[0]):
            return "Board #{} header should read {}."\
                   .format(cnt, re.sub(r'.*B\[', 'B[', std[0]))
        for i in range(2, len(std), 3):
            s1 = std[i]
            s2 = std[i+1]
            a1 = actual[i]
            a2 = actual[i+1]
            for j in range(1, len(s1), 7):
                s_num = s1[j:j+6]
                a_num = a1[j:j+6]
                if s_num != "      " and s_num != a_num:
                    return "Number discrepency on board #{}: {} should be {}."\
                           .format(cnt, a_num.strip(), s_num.strip())
                s_dir = s2[3:5]
                a_dir = a2[3:5]
                if (s_dir == " *") != (a_dir == " *"):
                    return "Wrong target square on board #{}.".format(cnt)
        if std[0][0:2] == "**":
            if re.search(r'[.o]', actual_board):
                return "Board #{} has unconnected squares.".format(cnt)
            try:
                directions = [ [ row[x:x+2] for row in actual[-1:0:-3] ]
                               for x in range(4, len(actual[1]), 7) ]
                actual_soln = [ [ int(row[x:x+5]) for row in actual[-2:0:-3] ]
                                for x in range(1, len(actual[1]), 7) ]
            except:
                return "Unknown format error on board #{}.".format(cnt)
            if not good_solution(directions, actual_soln):
                return "Board #{} has an invalid solution.".format(cnt)
    return True
                           
def good_solution(dirs, nums):
    size = len(nums) * len(nums[0])
    if { x for row in nums for x in row } != set(range(1, size + 1)):
        return False
    for x in range(len(nums)):
        for y in range(len(nums[x])):
            if nums[x][y] < size:
                for x1 in range(len(nums)):
                    for y1 in range(len(nums[x1])):
                        if nums[x][y] + 1 == nums[x1][y1]:
                            if dir_of(x, y, x1, y1) != dirs[x][y]:
                                return False
                            else:
                                break
    return True

def dir_of(x0, y0, x1, y1):
    if x0 == x1:
        return "N " if y1 > y0 else "S "
    if y0 == y1:
        return "E " if x1 > x0 else "W "
    if x0 + y0 == x1 + y1:
        return "NW" if y1 > y0 else "SE"
    if x0 - y0 == x1 - y1:
        return "NE" if y1 > y0 else "SW"
    return "??"

class Proj0_Tester(Tester):
    def output_filter(self, id, text):
        text = re.sub(r'#.*\r?\n','',text)
        return text

    def output_compare(self, testid):
        standard = self.standard_output_file(testid)
        if self.rc != 0:
            self.reason = "Program exited abnormally: {}" \
                              .format(interpret_problem(self.rc, self.stderr))
            return
        elif self.stdout is not None and standard:
            self.reason = compare_loose(self.output_filter(testid, self.stdout),
                                        self.output_filter(testid,
                                                           contents(standard)))
            if self.reason is not True:
                return
        if self.stderr:
            self.reason = "Error output is not empty."
        else:
            self.reason = True

show=None
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        else:
            assert False
except:
    print("Usage: python3 tester.py [--show=N] TEST.in...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj0_Tester(tested_program=PROGRAM, report_limit=show,
                      report_char_limit=10000)

sys.exit(0 if tester.test_all(args) else 1)

