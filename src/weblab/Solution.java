package weblab;

import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {
    public static int nQueens(int n) {
        Solver<Integer> mySolver = new Solver<>();

        Integer[][] domain = new Integer[n*n][2];

        for (int i = 0; i < n*n; i++) {
            Integer[] choice = new Integer[] {0,1};
            domain[i] = choice;
        }

        List<Constraint<Integer>> constraints = new ArrayList<>();

        Set<List<Integer>> known_solutions = new HashSet<>();
        Constraint<Integer> newConstraint = new Constraint<>(xs -> !known_solutions.contains(xs), new int[] {0});
        constraints.add(newConstraint);

        Constraint<Integer> directionsAndNumber = new Constraint<>(x -> {
            // System.out.println("inside constraint!");
            boolean[] vertical = new boolean[n];
            HashSet<Integer> set = new HashSet<>();
            int nr = 0;
            for (int i = 0; i < n; i++) {
                boolean see = false;
                for (int j = 0; j < n; j++) {
                    if(x.get(i*n+j) == 1) {
                        //See if something is in the row
                        if(see == true) {
                            return false;
                        }
                        see = true;
                        //See if something is in the column
                        if(vertical[j]) {
                            return false;
                        }
                        vertical[j] = true;
                        //See if something was on same diagonal
                        int plus = j+i+n;
                        int minus = j-i-n;
                        if (set.contains(plus) || set.contains(minus)) {
                            return false;
                        }
                        set.add((j+i) + n);
                        set.add((j-i) - n);
                        nr++;
                    }
                }
            }
            return nr == n;
        }, new int[] {(n*n)-1});
        constraints.add(directionsAndNumber);

        // Constraint<Integer> idk = new Constraint<>(xs -> nr != n, new int[] {(int) Math.pow(n, 2)});
        // constraints.add(idk);

        List<List<Integer>> solutions = new ArrayList<>();
        List<Integer> solution = mySolver.backTracking_helper(domain, constraints);

        while (solution != null) {
            System.out.println("Adding a new solution: " + solution);
            solutions.add(solution);
            known_solutions.add(solution);

            solution = mySolver.backTracking_helper(domain, constraints);
        }

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution.size();
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
        int tmp = nQueens(2);
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
