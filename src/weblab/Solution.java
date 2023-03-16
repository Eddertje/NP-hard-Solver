package weblab;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solution {
    public static List<Integer> exampleProblem(int n) {

        //Binary Strings
        Solver<Integer> mySolver = new Solver<>();
        Integer[][] domain = new Integer[n][];
        for (int i = 0; i < n; i++) {
            domain[i] = new Integer[] {0, 1};
        }

        List<Constraint<Integer>> constraints = new ArrayList<>();
        constraints.add(new Constraint<>((a) -> a.get(0) - a.get(2) >= 0, new int[]{0, 2}));
        constraints.add(new Constraint<>((a) -> a.get(1) != 0, new int[]{1}));

        System.out.println("Constraints: " + constraints);

        List<Integer> solution = mySolver.backTracking_helper(domain, constraints);

        System.out.println("A solution: " + solution);
        return solution;
    }

    public static List<String> getBinaryStrings(int n) {

        Solver<Character> mySolver = new Solver<>();
        Character[][] domain = new Character[n][];
        for (int i = 0; i < n; i++) {
            domain[i] = new Character[] {'0', '1'};
        }

        List<Constraint<Character>> constraints = new ArrayList<>();

        List<List<Character>> all_solutions = mySolver.multiple_backtrack(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static void main(String[] args) {
        // exampleProblem(3);
        getBinaryStrings(20);
        //permutationsNoRepetitions(3, 2);
        //permutationsWithRepetitions(3, 2);
        //subSets(3);
        //setPermutations(4);
    }
}

class Solver<A> {
    public List<List<A>> multiple_backtrack(A[][] domain, List<Constraint<A>> constraints) {
        List<List<A>> all_solutions = new ArrayList<>();
        Set<List<A>> known_solutions = new HashSet<>();
        int[] variables = new int[domain.length];
        for (int i = 0; i < domain.length; i++) {
            variables[i] = i;
        }
        Constraint<A> no_duplicates = new Constraint<>(a -> !known_solutions.contains(a), variables);
        constraints.add(no_duplicates);

        List<A> sol = backTracking_helper(domain, constraints);
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

    private List<A> getSolution(A[][] domain, List<Integer> variables) {
        List<A> solution = new ArrayList<>(variables.size());
        for (int i = 0; i < variables.size(); i++) {
            solution.add(domain[i][variables.get(i)]);
        }
        return solution;
    }

    private boolean isValid(List<A> assignment, Constraint<A> constraint) {
        return constraint.eval.apply(assignment);
    }

    private boolean isConsistent(A[][] domain, List<Integer> partial_solution, List<Constraint<A>> constraints) {
        for (Constraint<A> constraint : constraints) {
            boolean will_check = true;
            for (int variable : constraint.variables) {
                if (variable > partial_solution.size()-1) {
                    // System.out.println("Will not check!");
                    will_check = false;
                }
            }

            if (will_check) {
                if (!isValid(getSolution(domain, partial_solution), constraint)) {
                    return false;
                }
                // System.out.println("This solution is valid!");
            }
        }
        return true;
    }

    public List<A> backTracking_helper(A[][] domain, List<Constraint<A>> constraints) {
        List<Integer> unassigned = new ArrayList<>();
        return backTracking(0, unassigned, domain, constraints);
    }

    private List<A> backTracking(int depth, List<Integer> solution, A[][] domain, List<Constraint<A>> constraints) {
        if (depth == domain.length) {
            return getSolution(domain, solution);
        }

        for (int i = 0; i < domain[depth].length; i++) {
            List<Integer> new_solution = new ArrayList<>(solution);
            new_solution.add(i);

            if (isConsistent(domain, new_solution, constraints)) {
                List<A> potential_solution = backTracking(depth+1, new_solution, domain, constraints);
                if (potential_solution != null) {
                    // System.out.println("Returning: " + potential_solution);
                    return potential_solution;
                }
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
