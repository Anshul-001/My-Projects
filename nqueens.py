# Author: Anshul Pakala
import random
import sys


def succ(state, static_x, static_y):
    boardLimit = len(state)
    succList = []
    successors = []
    # If there is no queen at the static point return empty list.
    if state[static_x] != static_y:
        return []
    # Did not learn what enumerate was till I wrote the next function.
    # Therefore, probably not the cleanest way, but the entire program still runs
    # under 0.015 seconds.
    for i in range(boardLimit):
        # Move Queen +1 index if it can.
        if 0 <= state[i] < boardLimit - 1:
            temp_state = state.copy()
            temp_state[i] = temp_state[i] + 1
            succList.append(temp_state.copy())
        # Move Queen -1 index if it can.
        if boardLimit - 1 >= state[i] > 0:
            temp_state = state.copy()
            temp_state[i] = temp_state[i] - 1
            succList.append(temp_state.copy())
    # Only return states that have a queen at the static point.
    for succState in succList:
        if succState[static_x] == static_y:
            successors.append(succState)
    return sorted(successors)


def f(state):
    fScore = 0
    # Learnt what enumerate was here, made it much easier for me.
    for x, y in enumerate(state):
        for x2, y2 in enumerate(state):
            # Assume queens cannot attack each other vertically.
            if x != x2:
                # Check if Queens are attacking horizontally.
                if y == y2:
                    fScore = fScore + 1
                    break
                # If slope of the line is -1 or 1, the Queens are attacking on the diagonal.
                elif abs(x - x2) == abs(y - y2):
                    fScore += 1
                    break
    return fScore


def choose_next(curr, static_x, static_y):
    f_scores_succ = []
    nextState = []
    successors = succ(curr, static_x, static_y)
    # If there are no valid successors:
    if len(successors) == 0:
        return None
    # If there is only one successor:
    if len(successors) == 1:
        return successors[0]
    # Add current state to list, return it later if it is the best.
    successors.append(curr)
    # Find f scores for all successors and current state.
    for state in successors:
        f_scores_succ.append(f(state))
    # Since the f scores are appended in the same order, we can use enumerate for finding index.
    for index, state in enumerate(successors):
        # Return the states that have the minimum f scores.
        if f_scores_succ[index] == min(f_scores_succ):
            nextState.append(state)
    return sorted(nextState)[0]


def n_queens(initial_state, static_x, static_y, print_path=True):
    fScore = 0
    current_state = initial_state.copy()
    # Make sure two next_states don't have the same fScores. Return if they do.
    while f(current_state) != 0 and f(current_state) != fScore:
        fScore = f(current_state)
        # Source: Piazza @495 Yufei Wang
        if print_path:
            print(str(current_state) + " - f=" + str(fScore))
        current_state = choose_next(current_state, static_x, static_y)
    if print_path:
        print(str(current_state) + " - f=" + str(f(current_state)))
    return current_state


def n_queens_restart(n, k, static_x, static_y):
    random.seed(1)
    # In the previous assignment i assigned 1000000 as the value for minimum.
    # Here I found the alternative to JAVA's Integer.MIN_VALUE. We want the first f to be the minimum
    min_fScore = sys.maxsize
    min_states = []
    for i in range(k):
        # Build a n x n board.
        current_state = build_random_state(n, static_x, static_y)
        result_state = n_queens(current_state, static_x, static_y, False)
        current_fScore = f(result_state)
        # If f score is the least possible (0), empty list and add current state.
        if current_fScore == 0 and min_fScore != 0:
            min_states = [result_state]
        # If f score is the same, add to list.
        if current_fScore == min_fScore:
            min_states.append(result_state)
        # If f score is smaller than min, empty list, add current state, and update min score.
        if current_fScore < min_fScore:
            min_states = [result_state]
            min_fScore = current_fScore
    for state in sorted(min_states):
        print(str(state) + " - f=" + str(f(state)))


def build_random_state(n, static_x, static_y):
    state = []
    for row in range(n):
        # Put queen at static point.
        if row == static_x:
            state.append(static_y)
            continue
        col = random.randint(0, n - 1)
        state.append(col)
    return state

