from collections import Counter
try:
   f = open("input.txt")
except:
   f = open("day4/input.txt")
lines = f.read().split('\n')
valid=0
for line in lines:
#   print(line)
   words = line.split()
#   print(words)
   c = Counter(words).most_common(1)
#   print(c)
#   print(c[0][1]==1)
   if(c[0][1]==1):
      valid+=1
print(valid)