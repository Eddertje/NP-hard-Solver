package weblab;

import java.util.*;
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
        List<Constraint<List<Integer>, Boolean>> constraints = new ArrayList<>();
        constraints.add(new Constraint<>((a) -> a.get(0) - a.get(2) >= 0, new int[]{0, 2}));
        constraints.add(new Constraint<>((a) -> a.get(1) != 0, new int[]{1}));

        List<Integer> solution = mySolver.getOneSolution(domain, constraints);

        System.out.println("A solution: " + solution);
        return solution;
    }

    public static void main(String[] args) {
        exampleProblem(3);
        //getBinaryStrings(3);
        //permutationsNoRepetitions(3, 2);
        //permutationsWithRepetitions(3, 2);
        //subSets(3);
        //setPermutations(4);
    }
}

class Solver<A> {
    public List<List<A>> getAllSolutions(A[][] domain, List<Constraint<List<A>, Boolean>> constraints) {
        Set<List<A>> known_solutions = new HashSet<>();
        List<A> solution = getOneSolution(domain, constraints);
        while (solution != null) {
            System.out.println("Exploring a solution: " + known_solutions.size());
            known_solutions.add(solution);
            solution = getOneSolution(domain, constraints);
        }

        return new ArrayList<>(known_solutions);
    }

    public List<A> getOneSolution(A[][] domain, List<Constraint<List<A>, Boolean>> constraints) {
        Integer[] search = new Integer[domain.length];
        List<A> potential_solution;

        do {
            A[][] backtrackDomain = backtracking(domain, constraints, search);
            potential_solution = createPotentialSolutions(backtrackDomain, search);

            if (isValid(potential_solution, constraints)) {
                return potential_solution;
            }
        } while (generateNext(domain, search)); // TODO: update generate next in relation to new domain setup.

        return null;
    }

    private boolean isValid(List<A> solution, List<Constraint<List<A>, Boolean>> constraints) {
        System.out.println("Checking the validity of: " + solution);
        if (solution == null) {
            return false;
        }
        for (Constraint<List<A>, Boolean> constraint : constraints) {
            System.out.println("Checking constraint: " + constraint);
            if (!constraint.eval.apply(solution)) {
                System.out.println("Failed at: " + constraint);
                return false;
            }
        }
        System.out.println("Passed all checks!");
        return true;
    }

    private A[][] backtracking(A[][] domain, List<Constraint<List<A>, Boolean>> constraints, Integer[] search) {
        // null value in search has not been set, other values have.
        // A: For each value that has not yet been set
        int A = -1;
        for (int i = 0; i < search.length; i++) {
            if (search[i] == null) {
                // this is A
                A = i;
            }
        }
        System.out.println("A variable that has not been set: " + A);
        // B: If it is the only non-set value for a constraint.
        for (var constraint : constraints) {
            if (count_nonset_variable(constraint, search) == 1) { // find out if our variable is the only nonset variable.
                for (int i = 0; i < domain[A].length; i++) {
                    System.out.println("Checking: " + domain[A][i]);
                    Integer tmp = search[A];
                    search[A] = i;
                    if (isValid(createPotentialSolutions(domain, search), constraints)) {
                        System.out.println("Invalid solution, removing from domain!");
                    }
                    search[A] = tmp;
                }
            }
        }
        // Exhaustively search through all possible values.
        // Remove from the domain those that do not lead to a possible solution.

        return domain;
    }

    private int count_nonset_variable(Constraint<List<A>, Boolean> constraint, Integer[] search) {
        int count = 0;
        for (int i = 0; i < search.length; i++) {
            if (Arrays.stream(constraint.variables).anyMatch(search[i]::equals) && search[i] == null) {
                count++;
            }
        }
        return count;
    }

    private boolean generateNext(A[][] domain, Integer[] previous) {
        for (int i = domain.length-1; i >= 0; --i) {
            if (previous[i] >= domain[i].length-1) {
                previous[i] = 0;
            } else {
                previous[i]++;
                return true;
            }
        }
        return false;
    }

    private List<A> createPotentialSolutions(A[][] domain, Integer[] search) {
        List<A> potential_solution = new ArrayList<>();
        for (int index = 0; index < search.length; index++) {
            potential_solution.add(domain[index][search[index]]);
        }

        return potential_solution;
    }
}

interface Lambda<A, B> {
    A eval(B a);
}

class Constraint<A, B> {
    Function<A, B> eval;
    int[] variables;

    public Constraint(Function<A, B> func, int[] variables) {
        this.eval = func;
        this.variables = variables;
    }
}
