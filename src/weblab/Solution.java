package weblab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solution {
    public static List<String> getBinaryStrings(int n) {
        Set<List<Character>> solutions = new HashSet<>();

        Solver<Character> mySolver = new Solver<>();
        Character[][] domain = new Character[n][];
        for (int i = 0; i < n; i++) {
            domain[i] = new Character[] {'0', '1'};
        }

        List<Function<List<Character>, Boolean>> constraints = new ArrayList<>();

        List<Character> solution = mySolver.solve(domain, constraints);
        while (solution != null) {
            solutions.add(solution);

            // Add the existing solution as a constraint
            List<Character> copiedSolution = new ArrayList<>(solution);
            Function<List<Character>, Boolean> newConstraint = (a) -> a.equals(new ArrayList<>(copiedSolution));
            constraints.add(newConstraint);

            solution = mySolver.solve(domain, constraints);
        }

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static void main(String[] args) {
        getBinaryStrings(3);
    }
}

class Solver<A> {
    public boolean generate_next(A[][] domain, int[] previous) {
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

    public List<A> solve(A[][] domain, List<Function<List<A>, Boolean>> constraints) {
        int[] search = new int[domain.length];
        List<A> potential_solution;

        do {
            potential_solution = create_potential_solution(domain, search);

            if (isValid(potential_solution, constraints)) {
                return potential_solution;
            }
        } while (generate_next(domain, search));

        return null;
    }

    private boolean isValid(List<A> solution, List<Function<List<A>, Boolean>> constraints) {
        if (solution == null) {
            return false;
        }
        for (Function<List<A>, Boolean> constraint : constraints) {
            if (constraint.apply(solution)) {
                return false;
            }
        }
        return true;
    }

    private List<A> create_potential_solution(A[][] domain, int[] search) {
        List<A> potential_solution = new ArrayList<>();
        for (int index = 0; index < search.length; index++) {
            potential_solution.add(domain[index][search[index]]);
        }

        return potential_solution;
    }
}
