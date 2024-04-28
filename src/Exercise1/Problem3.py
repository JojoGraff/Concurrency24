import matplotlib.pylab as plt

serializable = float(input("Enter the serializable part (e.g., 0.01): "))
core_range = int(input("Enter the maximum number of cores: "))
parallel = 1 - serializable
print("serializable part: " + str(serializable))
print("parallel part: " + str(parallel))

cores_list = []
speedup_list = []


for cores in range(1, core_range):
    speedup = 1 / (serializable + (parallel / cores))
    cores_list.append(cores)
    speedup_list.append(speedup)

max_speedup = max(speedup_list)
max_speedup_core = cores_list[speedup_list.index(max_speedup)]
print("maximum speedup is: " + str(max_speedup) + " and can be achieved with " + str(max_speedup_core) + " cores")

plt.figure(figsize=(10, 6))
plt.plot(cores_list, speedup_list, marker='o', linestyle='-', color='b')
plt.xlabel("Cores")
plt.ylabel("Speedup")
plt.title("Speedup with Cores")
plt.grid(True)
plt.show()