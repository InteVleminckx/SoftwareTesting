import os
import random
import subprocess
import sys
import time

MUTATE = False
INPUT_MAP = ["WWWWWWW\n",
             "W00000W\n",
             "W00M00W\n",
             "W0P000W\n",
             "W000F0W\n",
             "WWWWWWW\n"] # for mutation
INPUT_ACTION_SEQ = "SUDDRRWE" # for mutation
UNIQUE_CRASHES = {}

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

def save_map_to_file(map_str):
    with open("fuzz.map", "w") as f:
        f.write(map_str)
    return map_str

def generate_binary_map():
    with open("fuzz.map", "wb") as f:
        input_map = os.urandom(random.randint(1, 100))
        f.write(input_map)
    return input_map

def generate_valid_map():

    valid_char_list = "FW0M"
    amount_of_rows = random.randint(1, 50)
    length_row = random.randint(1, 50)
    gen_map = ""
    p_row = random.randint(0, amount_of_rows - 1)
    p_col = random.randint(0, length_row - 1)
    with open("fuzz.map", "w") as f:
        # player, food, lengths most be identical, valid characters, valid file
        # TODO: check food in string
        for row in range(amount_of_rows):
            gen_row = "".join(random.choices(valid_char_list, k=length_row))
            if row == p_row:
                # Replace a random position with 'P'
                gen_row = gen_row[:p_col] + 'P' + gen_row[p_col + 1:]
            gen_row += "\n"
            f.write(gen_row)
            gen_map +=  gen_row

    return gen_map

def mutate_map(valid_map):
    mutated_map = valid_map
    all_chars = "FW0M" # no player?
    num_mutations = random.randint(1, 10)

    for _ in range(num_mutations):
        row_idx = random.randint(0, len(mutated_map) - 1)
        col_idx = random.randint(0, len(mutated_map[row_idx]) - 2) # -2 to avoid newline
        row = list(mutated_map[row_idx])
        row[col_idx] = random.choice(all_chars)
        mutated_map[row_idx] = "".join(row)

    return mutated_map

def mutate_action_sequence(action_seq):
    seq = list(action_seq)
    valid_chars = "SUDLRWQE"

    for _ in range(random.randint(1, 5)):
        seq[random.randint(1, len(seq) - 2)] = random.choice(valid_chars) # replace char at random position

    if random.random() < 0.5: # chance for insert at random position
        seq.insert(random.randint(1, len(seq) - 1), random.choice(valid_chars))
    else: # chance for delete at random position
        seq.pop(random.randint(1, len(seq) - 2))

    return ''.join(seq)

def generate_action_sequence():
    return "S" + "".join(random.choices("ESUDQWLR", k=random.randint(1, 50))) + "E"


def run_jpacman(action_seq, manual=False):
    file = "fuzz.map" if not manual else "manual.map"
    try:
        result = subprocess.run([
            "java", "-jar", "jpacman-3.0.1.jar", file, action_seq
        ], capture_output=True, text=True)

        return result.returncode, result.stdout
    except Exception as e:
        return -1, e


def check_exit_code(result_file, input_map, action_seq, code, message, log_crashes_only=True):
    if log_crashes_only and code == 0:
        return
    exit_codes = {0: "PASS", 10: "REJECTION", 1: "CRASH"}
    # Adding quotes around strings allows to have newlines and commas in the cells
    message = f'"{message}"'
    map = f'"{input_map}"'
    result_file.write((f"{exit_codes.get(code, 'EXCEPTION ERROR')}\t"
            f"{message if len(str(message)) > 0 else 'Success'}\t"
            f"{map}\t{action_seq}\n"))

    # Store unique crashes
    if code == 1 or code == 10:
        if message not in UNIQUE_CRASHES:
            UNIQUE_CRASHES[message] = 1
            with open("unique_crashes.txt", "a") as f:
                f.write(f"{message}\n")
        else:
            UNIQUE_CRASHES[message] += 1


@fuzz_timer("TIME", 600)
def fuzz(result_file):
    if MUTATE:
        mutated_map = mutate_map(INPUT_MAP)
        mutated_action_seq = mutate_action_sequence(INPUT_ACTION_SEQ)
        saved_map = save_map_to_file("".join(mutated_map))
        code, message = run_jpacman(mutated_action_seq)
        check_exit_code(result_file, saved_map, mutated_action_seq, code, message)
    else:
        input_map = generate_valid_map()
        action_seq = generate_action_sequence()
        code, message = run_jpacman(action_seq)
        check_exit_code(result_file, input_map, action_seq, code, message)

def fuzz_manual(action_sequence):
    code, message = run_jpacman(action_sequence, manual=True)
    print(f"Code: {code}\nMessage: {message}")

if __name__ == "__main__":
    # fuzz()
    # print(UNIQUE_CRASHES)

    fuzz_manual("SWWWE")
    # fuzz_manual("SLESL")
    # fuzz_manual("SWWWW")
    # fuzz_manual("ESL")
