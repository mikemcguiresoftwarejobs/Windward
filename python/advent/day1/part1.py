try:
   f = open("input.txt")
except:
   f = open("day1/input.txt")
text = f.read()
counter=0
for index in range(0,len(text)-1):
   if text[index]==text[index+1]:
      counter+=int(text[index])
if text[len(text)-1]==text[0]:
   counter+=int(text[0])
print(counter)