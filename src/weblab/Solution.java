package weblab;

import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solution {
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        Solver<Integer> mySolver = new Solver<>();
        Integer[][] domain = new Integer[k][];

        for (int i = 0; i < k; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j<n; j++) {
                choice[j] = j+1;
            }
            domain[i] = choice;
        }


        List<Constraint<Integer>> constraints = new ArrayList<>();

        Constraint<Integer> largerThanSecondLargest = new Constraint<>(xs -> xs.get(xs.size()-2) < xs.get(xs.size()-1), new int[] {1});
        constraints.add(largerThanSecondLargest);

        List<List<Integer>> solutions = new ArrayList<>();
        List<Integer> solution = mySolver.backTracking_helper(domain, constraints);
        while (solution != null){
            solutions.add(getSolution(domain, solution));

            List<Constraint<Integer>> new_constraints = new ArrayList<>(constraints);
            List<Integer> finalSolution = solution;
            Constraint<Integer> largerThan = new Constraint<>(xs -> compareNumbers2(xs, finalSolution), new int[] {0});
            new_constraints.add(largerThan);

            solution = mySolver.backTracking_helper(domain, new_constraints);
        }

        return solutions
                .stream()
                .map(c -> c
                        .stream()
                        .mapToInt(x -> x).toArray())
                .collect(Collectors.toList());
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

    private static List<Integer> getSolution(Integer[][] domain, List<Integer> variables) {
        List<Integer> solution = new ArrayList<>(variables.size());
        for (int i = 0; i < variables.size(); i++) {
            solution.add(domain[i][variables.get(i)]);
        }
        return solution;
    }

    public static void main(String[] args) {
        // exampleProblem(3);

        long startTime = System.currentTimeMillis();
        // var solutions = getCombinationsWithoutRepetition(20, 8); // around 1 sec ish
        var solutions = getCombinationsWithoutRepetition(10, 2);
        long endTime = System.currentTimeMillis();

        System.out.println("Solutions: ");
        for (var sol : solutions) {
            System.out.println(Arrays.toString(sol));
        }

        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}

class Solver<A> {
    private boolean isValid(List<Integer> assignment, Constraint<Integer> constraint) {
        return constraint.eval.apply(assignment);
    }

    private boolean isConsistent(List<Integer> partial_solution, List<Constraint<Integer>> constraints) {
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
            System.out.println("Found a new solution: " + solution);
            return solution;
        }

        for (int i = 0; i < domain[depth].length; i++) {
            List<Integer> new_solution = new ArrayList<>(solution);
            new_solution.add(i);
            if (isConsistent(new_solution, constraints)) {
                // System.out.println("a new solution: " + new_solution);
                System.out.println("Is consistent: " + new_solution);
                List<Integer> potential_solution = backTracking(depth+1, new_solution, domain, constraints);
                if (potential_solution != null) {
                    return potential_solution;
                }
            } else {
                System.out.println("Is not consistent!" + new_solution);
            }
        }

        return null;
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
