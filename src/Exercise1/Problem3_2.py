import matplotlib.pylab as plt
import time

cores_list = []
speedup_list = []


while True:
    end_loop = input("Wanna provide input? (y / n)")
    if end_loop == "n":
        break

    serializable = float(input("serializable part (example: 0.01): "))
    cores = int(input("Enter number of cores: "))

    parallel = 1 - serializable
    print("serializable part: " + str(serializable))
    time.sleep(serializable * 10)
    print("finished serializable part")
    print("parallel part: " + str(parallel))
    time.sleep((parallel / cores) * 10)
    print("finished parallel part with " + str(cores) + " cores")



    speedup = 1 / (serializable + (parallel / cores))
    cores_list.append(cores)
    speedup_list.append(speedup)

plt.figure(figsize=(10, 6))
plt.plot(cores_list, speedup_list, marker='o', linestyle='', color='b')
plt.xlabel("Cores")
plt.ylabel("Speedup")
plt.title("Speedup with Cores")
plt.grid(True)
plt.show()