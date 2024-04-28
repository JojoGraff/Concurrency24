serializable = 0.01
parallel = 1 - serializable
cores = 64
print("serializable part: " + str(serializable))
print("parallel part: " + str(parallel))
print("cores: " + str(cores))
speedup = 1 / (serializable + (parallel / cores))
print("speed up: " + str(speedup))