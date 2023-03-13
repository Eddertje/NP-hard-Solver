package weblab;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Solution {
    public static List<String> getBinaryStrings(int n) {

        //Binary Strings
        Solver<Character> mySolver = new Solver<>();
        Character[][] domain = new Character[n][];
        for (int i = 0; i < n; i++) {
            domain[i] = new Character[] {'0', '1'};
        }
        List<Lambda<Boolean, List<Character>>> constraints = new ArrayList<>();
        Set<List<Character>> known_solutions = new HashSet<>();
        Lambda<Boolean, List<Character>> newConstraint = x -> {
            if (!known_solutions.contains(x)) {
                known_solutions.add(x);
                return false;
            }
            return true;
        };
        constraints.add(newConstraint);

        List<List<Character>> all_solutions = mySolver.getAllSolutions(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static List<String> permutationsNoRepetitions(int n, int k) {
        Solver<Integer> mySolver = new Solver<>();

        Integer[][] domain = new Integer[k][n];

        for (int i = 0; i < k; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j<n; j++) {
                choice[j] = j+1;
            }
            domain[i] = choice;
        }

        List<Lambda<Boolean, List<Integer>>> constraints = new ArrayList<>();

        Lambda<Boolean, List<Integer>> lambda = x -> {
            HashSet<Integer> in = new HashSet<>();
            for(int i : x) {
                if(in.contains(i)) {
                    return true;
                }
                in.add(i);
            }
            return false;
        };
        constraints.add(lambda);

        Set<Set<Integer>> known_solutions = new HashSet<>();
        Lambda<Boolean, List<Integer>> newConstraint = x -> {
            Set<Integer> set = new HashSet<>(x);
            if (!known_solutions.contains(set)) {
                known_solutions.add(set);
                return false;
            }
            return true;
        };
        constraints.add(newConstraint);

        List<List<Integer>> all_solutions = mySolver.getAllSolutions(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static List<String> permutationsWithRepetitions(int n, int k) {
        Solver<Integer> mySolver = new Solver<>();

        Integer[][] domain = new Integer[k][n];

        for (int i = 0; i < k; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j<n; j++) {
                choice[j] = j+1;
            }
            domain[i] = choice;
        }

        List<Lambda<Boolean, List<Integer>>> constraints = new ArrayList<>();

        Lambda<Boolean, List<Integer>> lambda = x -> {
            HashSet<Integer> in = new HashSet<>();
            for(int i : x) {
                if(in.contains(i)) {
                    return true;
                }
                in.add(i);
            }
            return false;
        };
        constraints.add(lambda);

        Set<List<Integer>> known_solutions = new HashSet<>();
        Lambda<Boolean, List<Integer>> newConstraint = x -> {
            if (!known_solutions.contains(x)) {
                known_solutions.add(x);
                return false;
            }
            return true;
        };
        constraints.add(newConstraint);

        List<List<Integer>> all_solutions = mySolver.getAllSolutions(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static List<String> subSets(int n) {

        //Binary Strings
        Solver<Integer> mySolver = new Solver<>();
        Integer[][] domain = new Integer[n][];
        for (int i = 0; i < n; i++) {
            domain[i] = new Integer[] {-1, i+1};
        }
        List<Lambda<Boolean, List<Integer>>> constraints = new ArrayList<>();
        Set<Set<Integer>> known_solutions = new HashSet<>();
        Lambda<Boolean, List<Integer>> newConstraint = x -> {
            Set<Integer> set = new HashSet<>(x);
            if(set.contains(-1)) {
                set.remove(-1);
            }
            if(set.isEmpty()) return true;
            if (!known_solutions.contains(set)) {
                known_solutions.add(set);
                return false;
            }
            return true;
        };
        constraints.add(newConstraint);

        List<List<Integer>> all_solutions = mySolver.getAllSolutions(domain, constraints);
        System.out.println("hi");
        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = known_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());
        Collections.sort(finalSolution);
        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static List<String> setPermutations(int n) {
        Solver<Integer> mySolver = new Solver<>();

        Integer[][] domain = new Integer[n][n];

        for (int i = 0; i < n; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j<n; j++) {
                choice[j] = j+1;
            }
            domain[i] = choice;
        }

        List<Lambda<Boolean, List<Integer>>> constraints = new ArrayList<>();

        Lambda<Boolean, List<Integer>> lambda = x -> {
            HashSet<Integer> in = new HashSet<>();
            for(int i : x) {
                if(in.contains(i)) {
                    return true;
                }
                in.add(i);
            }
            return false;
        };
        constraints.add(lambda);

        Set<List<Integer>> known_solutions = new HashSet<>();
        Lambda<Boolean, List<Integer>> newConstraint = x -> {
            if (!known_solutions.contains(x)) {
                known_solutions.add(x);
                return false;
            }
            return true;
        };
        constraints.add(newConstraint);

        List<List<Integer>> all_solutions = mySolver.getAllSolutions(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }
    public static void main(String[] args) {
        //getBinaryStrings(3);
        //permutationsNoRepetitions(3, 2);
        //permutationsWithRepetitions(3, 2);
        //subSets(3);
        //setPermutations(4);
    }
}

class Solver<A> {
    public List<List<A>> getAllSolutions(A[][] domain, List<Lambda<Boolean, List<A>>> constraints) {
        Set<List<A>> known_solutions = new HashSet<>();
        List<A> solution = getOneSolution(domain, constraints);
        while (solution != null) {
            known_solutions.add(solution);
            solution = getOneSolution(domain, constraints);
        }

        return new ArrayList<>(known_solutions);
    }

    public List<A> getOneSolution(A[][] domain, List<Lambda<Boolean, List<A>>> constraints) {
        int[] search = new int[domain.length];
        List<A> potential_solution;

        do {
            potential_solution = createPotentialSolutions(domain, search);

            if (isValid(potential_solution, constraints)) {
                return potential_solution;
            }
        } while (generateNext(domain, search));

        return null;
    }

    private boolean isValid(List<A> solution, List<Lambda<Boolean, List<A>>> constraints) {
        if (solution == null) {
            return false;
        }
        for (Lambda<Boolean, List<A>> constraint : constraints) {
            if (constraint.eval(solution)) {
                return false;
            }
        }
        return true;
    }

    private boolean generateNext(A[][] domain, int[] previous) {
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

    private List<A> createPotentialSolutions(A[][] domain, int[] search) {
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
