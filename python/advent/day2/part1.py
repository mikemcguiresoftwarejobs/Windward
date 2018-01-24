
def read_input():
   try:
      f = open("input.txt")
   except:
      f = open("day2/input.txt")
   return f.read().split('\n')

checksum=0
for line in read_input():
   numbers = [int(char) for char in line.split()]
   checksum += max(numbers)-min(numbers)
print(checksum)