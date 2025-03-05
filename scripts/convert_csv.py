import csv

input_file = "./scripts/benchmark_results.txt"
output_file = "./scripts/benchmark_results.csv"

def convert_to_csv(input_file, output_file):
    with open(input_file, "r") as infile, open(output_file, "w", newline='') as outfile:
        writer = csv.writer(outfile)
        for line in infile:
            parts = line.split()
            if parts:
                writer.writerow(parts)

# Convert the benchmark file to CSV
convert_to_csv(input_file, output_file)