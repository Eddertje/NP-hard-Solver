package weblab;

import java.util.*;
import java.util.List;
import java.util.function.Function;

class Solution {

    public static int[][] solve(int[][] grid) {
        Solver<Integer> mySolver = new Solver<>();
        Integer[][] domain = new Integer[81][];
        Integer[] sudoku = {1,2,3,4,5,6,7,8,9};
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(grid[i][j] != -1) {
                    domain[i+9*j] = new Integer[]{grid[i][j]};
                } else domain[i+9*j] = sudoku;
            }
        }

        List<Constraint<Integer>> constraints = new ArrayList<>();

        Constraint<Integer> horizontal = new Constraint<>(x -> {
            Set<Integer>[] lines = new HashSet[9];
            for (int i = 0; i < 9; i++) {
                lines[i] = new HashSet<>();
            }
            for (int i = 0; i < x.size(); i++) {
                if(lines[i/9].contains(x.get(i))){
                    System.out.println(lines[i/9]);
                    System.out.println(i);
                    System.out.println(x.get(i));
                    System.out.println("hi");
                    return false;
                }
                lines[i/9].add(x.get(i));
            }
            return true;
        }, new int[] {1});
        constraints.add(horizontal);

        Constraint<Integer> vertical = new Constraint<>(x -> {
            Set<Integer>[] lines = new HashSet[9];
            for (int i = 0; i < 9; i++) {
                lines[i] = new HashSet<>();
            }
            for (int i = 0; i < x.size(); i++) {
                if(lines[i%9].contains(x.get(i))){
                    System.out.println("");
                    System.out.println(lines[i%9]);
                    System.out.println("hi2");
                    return false;
                }
                lines[i%9].add(x.get(i));
            }
            return true;
        }, new int[] {1});
        constraints.add(vertical);

        Constraint<Integer> boxes = new Constraint<>(x -> {
            Set<Integer>[][] box = new HashSet[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    box[i][j] = new HashSet<>();
                }
            }
            for (int i = 0; i < x.size(); i++) {
                int h = i/9;
                int v = i%9;
                if(box[h/3][v/3].contains(x.get(i))){
                    System.out.println("hi3");
                    return false;
                }
                box[h/3][v/3].add(x.get(i));
            }
            return true;
        }, new int[] {1});
        constraints.add(boxes);

        List<Integer> solution = mySolver.backTracking_helper(domain, constraints);

        for (int i = 0; i < 81; i++) {
            int h = i/9;
            int v = i%9;
            grid[h][v] = solution.get(i);
        }

        return grid;
    }
    public static void main(String[] args) {
        // exampleProblem(3);
        int[][] sudoku = new int[][]{{9,5,6,8,3,7,4,2,1},
                {8,7,4,2,1,6,9,5,3},
                {3,2,1,4,9,5,6,7,8},
                {6,9,5,3,7,4,1,8,2},
                {7,3,2,9,8,1,5,6,4},
                {4,1,8,6,5,2,7,3,9},
                {5,8,7,1,4,3,2,9,6},
                {1,6,3,5,2,9,8,4,7},
                {2,4,9,7,6,8,3,1,5}};

        long startTime = System.currentTimeMillis();
        int[][] tmp = solve(sudoku);
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
