import os
import random
import subprocess
import sys
import time

def fuzz_timer(stop_option, value):
    def decorator(func):
        def wrapper():
            with open("fuzz_results.tsv", "w") as f:
                f.write("Exit Code\tError message\tMap\tAction Sequence\n")
                if stop_option == "MAX":
                    sys.stdout.write(f"\rProgress iterations: {str(0).zfill(len(str(value)))}/{value}")
                    for i in range(value):
                        func(f)
                        sys.stdout.flush()
                        sys.stdout.write(f"\rProgress iterations: {str(i + 1).zfill(len(str(value)))}/{value}")
                else:
                    start_time = time.time()
                    elapsed = (time.time() - start_time)
                    sys.stdout.write(f"\rElapsed time: {str(round(elapsed)).zfill(len(str(value)))}/{value} seconds")
                    while elapsed < value:
                        func(f)
                        elapsed = (time.time() - start_time)
                        sys.stdout.write(f"\rElapsed time: {str(round(elapsed)).zfill(len(str(value)))}/{value} seconds")
        return wrapper
    return decorator

def generate_binary_map():
    with open("fuzz.map", "wb") as f:
        input_map = os.urandom(random.randint(1, 100))
        f.write(input_map)
    return input_map


def generate_action_sequence():
    return "".join(random.choices("ESUDQWLR", k=random.randint(1, 50)))


def run_jpacman(action_seq, manual=False):
    file = "fuzz.map" if not manual else "manual.map"
    try:
        result = subprocess.run([
            "java", "-jar", "jpacman-3.0.1.jar", file, action_seq
        ], capture_output=True, text=True)

        return result.returncode, result.stdout
    except Exception as e:
        return -1, e


def check_exit_code(result_file, input_map, action_seq, code, message):
    exit_codes = {0: "PASS", 10: "REJECTION", 1: "CRASH"}
    # Adding quotes around strings allows to have newlines and commas in the cells
    message = f'"{message}"'
    map = f'"{input_map}"'
    result_file.write((f"{exit_codes.get(code, 'EXCEPTION ERROR')}\t"
            f"{message if len(str(message)) > 0 else 'Success'}\t"
            f"{map}\t{action_seq}\n"))


@fuzz_timer("MAX", 1000)
def fuzz(result_file):
    input_map = generate_binary_map()
    action_seq = generate_action_sequence()
    code, message = run_jpacman(action_seq)
    check_exit_code(result_file, input_map, action_seq, code, message)

def fuzz_manual(action_sequence):
    code, message = run_jpacman(action_sequence, manual=True)
    print(f"Code: {code}\nMessage: {message}")

if __name__ == "__main__":
    # fuzz()
    fuzz_manual("")
