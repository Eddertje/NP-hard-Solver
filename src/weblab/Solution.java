package weblab;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        List<List<Character>> all_solutions = mySolver.backTracking_all(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static List<int[]> permutationsNoRepetitions(int n, int k) {
        Solver<Integer> mySolver = new Solver<>();

        Integer[][] domain = new Integer[k][n];

        for (int i = 0; i < k; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j<n; j++) {
                choice[j] = j+1;
            }
            domain[i] = choice;
        }

        System.out.println("Domain: " + Arrays.deepToString(domain));

        List<Constraint<Integer>> constraints = new ArrayList<>();

        Constraint<Integer> constraint = new Constraint<>((xs) -> {
            HashSet<Integer> in = new HashSet<>();
            for(int i : xs) {
                if(in.contains(i)) {
                    return true;
                }
                in.add(i);
            }
            return false;
        }, IntStream.rangeClosed(0, k-1).toArray());
        constraints.add(constraint);

        List<List<Integer>> all_solutions = mySolver.backTracking_all(domain, constraints);
        List<int[]> finalSolution = all_solutions.stream().map(xs -> xs.stream().mapToInt(x->x).toArray()).collect(Collectors.toList());

        for (int[] solution : finalSolution) {
            System.out.println("Found a solution : " + Arrays.toString(solution));
        }
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

        // List<Lambda<Boolean, List<Integer>>> constraints = new ArrayList<>();
        List<Constraint<Integer>> constraints = new ArrayList<>();
        Constraint<Integer> constraint = new Constraint<>(xs -> {
            HashSet<Integer> in = new HashSet<>();
            for(int i : xs) {
                if(in.contains(i)) {
                    return true;
                }
                in.add(i);
            }
            return false;
        }, IntStream.rangeClosed(0, n).toArray());
        constraints.add(constraint);

        List<List<Integer>> all_solutions = mySolver.multiple_backtrack(domain, constraints);

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
        List<Constraint<Integer>> constraints = new ArrayList<>();

        List<List<Integer>> all_solutions = mySolver.backTracking_all(domain, constraints);
        System.out.println("hi");
        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());
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

        List<Constraint<Integer>> constraints = new ArrayList<>();

        Constraint<Integer> constraint = new Constraint<>(xs -> {
            HashSet<Integer> in = new HashSet<>();
            for(int i : xs) {
                if(in.contains(i)) {
                    return true;
                }
                in.add(i);
            }
            return false;
        }, IntStream.rangeClosed(0, n).toArray());

        List<List<Integer>> all_solutions = mySolver.backTracking_all(domain, constraints);

        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());

        System.out.println("All solutions: " + finalSolution);
        return finalSolution;
    }

    public static void main(String[] args) {
        // exampleProblem(3);

        long startTime = System.currentTimeMillis();
        // getBinaryStrings(20);
        permutationsNoRepetitions(3, 2);
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

    public List<List<A>> backTracking_all(A[][] domain, List<Constraint<A>> constraints) {
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
        return solutions_at_depth.stream().map(a -> getSolution(domain, a)).collect(Collectors.toList());
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
