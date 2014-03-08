import sys
filename = sys.argv[1]

f = open(filename, 'r')
timing = []
for line in f:
    fields = line.split()
    if fields[-1] == "secs":
        timing.append(float(fields[-2]))

f.close()

print("MAX LATENCY: " + str(max(timing)) + " seconds")
print("MIN LATENCY: " + str(min(timing)) + " seconds")
print("AVERAGE LATENCY: " + str(sum(timing) / float(len(timing))) + " seconds")
