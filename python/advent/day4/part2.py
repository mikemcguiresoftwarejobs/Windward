from collections import Counter
try:
   f = open("input.txt")
except:
   f = open("day4/input.txt")
lines = f.read().split('\n')
print(len([1 for line in lines if Counter([''.join(sorted(word)) for word in line.split()]).most_common(1)[0][1]==1]))