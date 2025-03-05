import csv
import matplotlib.pyplot as plt # type: ignore
import numpy as np # type: ignore

# Sample input file name (change as needed)
input_file = "./scripts/benchmark_results.csv"
output_file = "./scripts/benchmark_plot.png"  # Output file name

# Function to parse scientific notation (e.g., "10^(-5)")
def parse_time(time_str):
    if not time_str:
        return 0.0
    if "^" in time_str:
        base, exponent = time_str.split("^")
        exponent = exponent.strip("()")
        return float(base) * (10 ** int(exponent))
    if "±" in time_str:
        time_str = time_str.split("±")[0].strip()
    try:
        return float(time_str)
    except ValueError:
        print(f"Error parsing time: {time_str}")
        return 0.0

# Parse the CSV file
benchmark_data = []
with open(input_file, "r") as file:
    reader = csv.reader(file)
    next(reader)  # Skip header
    for row in reader:
        if len(row) == 6:
            structure, method, _, _, time_str, _ = row
            print(f"Parsing row: {row}")  # Debug print
            time = parse_time(time_str)
            benchmark_data.append((structure, time))

# Debug print to check parsed data
print("Parsed benchmark data:", benchmark_data)

# Calculate differences between pairs
differences = []
x_labels = []
for i in range(0, len(benchmark_data), 2):
    if i + 1 < len(benchmark_data):
        structure1, time1 = benchmark_data[i]
        structure2, time2 = benchmark_data[i + 1]
        diff = time1 - time2
        if diff != 0:  # Exclude instances where the subtraction result is 0
            differences.append(diff)
            if "Custom" in structure1:
                x_labels.append(structure1.split("Custom")[1])
            else:
                x_labels.append(structure1.split("test")[1])

# Normalize the differences using z-score normalization
mean_diff = np.mean(differences)
std_diff = np.std(differences)
normalized_differences = [(diff - mean_diff) / std_diff for diff in differences]

# Debug print to check differences and labels
print("Differences:", differences)
print("Normalized Differences:", normalized_differences)
print("X labels:", x_labels)

# Plotting the data
plt.figure(figsize=(12, 6))
x = np.arange(len(x_labels))
width = 0.4

# Color bars based on their value
colors = ['red' if diff > 0 else 'blue' for diff in normalized_differences]

plt.bar(x, normalized_differences, width, color=colors)

plt.xticks(ticks=x, labels=x_labels, rotation=45, ha='right')
plt.ylabel("Normalized Difference in Execution Time (z-score)")
plt.xlabel("Benchmark Test")
plt.title("Normalized Difference in Execution Times: Custom vs. Java")
plt.grid(True, which='both', linestyle='--', linewidth=0.5, alpha=0.7)
plt.tight_layout()

# Save the plot as a .png file
plt.savefig(output_file)
print(f"Plot saved as {output_file}")