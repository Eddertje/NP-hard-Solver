package weblab;

import java.util.*;
import java.util.List;
import java.util.function.Function;

class Solution {
    public static int getNQueenSolutions(int n) {
        Solver<Integer> mySolver = new Solver<>();

        Integer[][] domain = new Integer[n][2];

        for (int i = 0; i < n; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j < choice.length; j++) {
                choice[j] = j;
            }
            domain[i] = choice;
        }

        List<Constraint<Integer>> constraints = new ArrayList<>();

        Set<List<Integer>> known_solutions = new HashSet<>();
        Constraint<Integer> newConstraint = new Constraint<>(xs -> !known_solutions.contains(xs), new int[] {n-1});
        constraints.add(newConstraint);

        Constraint<Integer> directions = new Constraint<>(x -> {
            // System.out.println("inside constraint!");
            boolean[] vertical = new boolean[n];
            HashSet<Integer> diagonal = new HashSet<>();
            for (int i = 0; i < x.size(); i++) {
                if (vertical[x.get(i)]) {
                    return false;
                }
                vertical[x.get(i)] = true;
                int left = x.get(i) - i - n;
                int right = x.get(i) + i + n;
                if (diagonal.contains(left) || diagonal.contains(right)) {
                    return false;
                }
                diagonal.add(left);
                diagonal.add(right);
            }
            if (x.size() == 4) {
                /*
                for (Integer j : x) {
                    System.out.println(j);
                }

                 *///System.out.println("hi");
            }
            return true;
        }, new int[] {1});
        constraints.add(directions);

        List<List<Integer>> solutions = new ArrayList<>();
        List<Integer> solution = mySolver.backTracking_helper(domain, constraints);

        while (solution != null) {
            //System.out.println("Adding a new solution: " + solution);
            solutions.add(solution);
            known_solutions.add(solution);
            List<Constraint<Integer>> new_constraints = new ArrayList<>(constraints);
            List<Integer> finalSolution1 = solution;
            Constraint<Integer> largerThanConstraint = new Constraint<>(xs -> compareNumbers2(xs, finalSolution1), new int[] {0});
            new_constraints.add(largerThanConstraint);

            solution = mySolver.backTracking_helper(domain, new_constraints);
        }

        //System.out.println("All solutions: " + finalSolution);
        return solutions.size();
    }

    private static boolean compareNumbers2(List<Integer> inputList, List<Integer> oldList) {
        for (int i = 0; i < inputList.size(); i++) {
            if (inputList.get(i) > oldList.get(i)) {
                return true;
            }
            if (inputList.get(i) < oldList.get(i)) {
                return false;
            }
        }
        return inputList.size() != oldList.size();
    }

    public static void main(String[] args) {
        // exampleProblem(3);

        long startTime = System.currentTimeMillis();
        int tmp = getNQueenSolutions(12);
        System.out.println("Result: " + tmp);
        // permutationsNoRepetitions(3, 2);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

        // 12: 2.1 sec
        // 13: 8.6 sec

        // permutationsNoRepetitions(3, 2);
        //permutationsWithRepetitions(3, 2);
        //subSets(3);
        //setPermutations(4);
    }
}

class Solver<A> {
    public List<List<Integer>> multiple_backtrack(A[][] domain, List<Constraint<Integer>> constraints) {
        List<List<Integer>> all_solutions = new ArrayList<>();
        Set<List<Integer>> known_solutions = new HashSet<>();
        int[] variables = new int[domain.length];
        for (int i = 0; i < domain.length; i++) {
            variables[i] = i;
        }
        Constraint<Integer> no_duplicates = new Constraint<>(a -> !known_solutions.contains(a), variables);
        constraints.add(no_duplicates);

        List<Integer> sol = backTracking_helper(domain, constraints);
        while (sol != null) {
            all_solutions.add(sol);
            known_solutions.add(sol);
            // System.out.println("all_solutions: " + all_solutions);
            // System.out.println("Known solutions: " + known_solutions);
            sol = backTracking_helper(domain, constraints);
            // System.out.println("sol: " + sol);
        }
        return all_solutions;
    }

    private boolean isValid(List<Integer> assignment, Constraint<Integer> constraint) {
        return constraint.eval.apply(assignment);
    }

    private boolean isConsistent(A[][] domain, List<Integer> partial_solution, List<Constraint<Integer>> constraints) {
        for (Constraint<Integer> constraint : constraints) {
            boolean will_check = true;
            for (int variable : constraint.variables) {
                if (variable > partial_solution.size()-1) {
                    // System.out.println("Variables: " + Arrays.toString(constraint.variables));
                    // System.out.println("Will not check: " + partial_solution);
                    will_check = false;
                }
            }

            if (will_check) {
                if (!isValid(partial_solution, constraint)) {
                    // System.out.println("This solution is not valid!: " + partial_solution);
                    return false;
                }
                // System.out.println("This solution is valid!: " + partial_solution);
            }
        }
        return true;
    }

    public List<Integer> backTracking_helper(A[][] domain, List<Constraint<Integer>> constraints) {
        List<Integer> unassigned = new ArrayList<>();
        return backTracking(0, unassigned, domain, constraints);
    }

    private List<Integer> backTracking(int depth, List<Integer> solution, A[][] domain, List<Constraint<Integer>> constraints) {
        if (depth == domain.length) {
            // System.out.println("Found a new solution: " + solution);
            return solution;
        }

        for (int i = 0; i < domain[depth].length; i++) {
            List<Integer> new_solution = new ArrayList<>(solution);
            new_solution.add(i);

            if (isConsistent(domain, new_solution, constraints)) {
                // System.out.println("Is consistent: " + new_solution);
                List<Integer> potential_solution = backTracking(depth+1, new_solution, domain, constraints);
                if (potential_solution != null) {
                    // System.out.println("Returning: " + potential_solution);
                    return potential_solution;
                }
            }/* else {
                System.out.println("Is not consistent!" + new_solution);
            }*/
        }

        return null;
    }

    public List<Integer> backTracking2(A[][] domain, List<Constraint<Integer>> constraints) {
        List<Integer> solution = new ArrayList<>();
        for (int depth = 0; depth < domain.length; depth++) {
            System.out.println("Considering variables at depth: " + depth);
            System.out.println("The solution so far: " + solution);
            for (int i = 0; i < domain[depth].length; i++) {
                List<Integer> new_solution = new ArrayList<>(solution);
                new_solution.add(i);

                if (isConsistent(domain, new_solution, constraints)) {
                    solution = new_solution;
                    break;
                }
            }
        }
        System.out.println("Returning a valid solution: " + solution);
        return solution;
    }

    public List<List<Integer>> backTracking_all(A[][] domain, List<Constraint<Integer>> constraints) {
        List<List<Integer>> solutions_at_depth = new ArrayList<>();
        solutions_at_depth.add(new ArrayList<>());
        for (int depth = 0; depth < domain.length; depth++) {
            List<List<Integer>> solutions_at_depth_new = new ArrayList<>();
            for (List<Integer> solution : solutions_at_depth) {
                for (int i = 0; i < domain[depth].length; i++) {
                    List<Integer> new_solution = new ArrayList<>(solution);
                    new_solution.add(i);

                    if (isConsistent(domain, new_solution, constraints)) {
                        solutions_at_depth_new.add(new_solution);
                    }
                }
            }
            solutions_at_depth = solutions_at_depth_new;
        }
        return solutions_at_depth;
    }
}

class Constraint<A> {
    public Function<List<A>, Boolean> eval;
    public int[] variables;
    public Constraint(Function<List<A>, Boolean> eval, int[] variables) {
        this.eval = eval;
        this.variables = variables;
    }
}
