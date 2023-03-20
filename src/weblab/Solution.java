package weblab;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

class Solution {
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        Solver<Integer> mySolver = new Solver<>();
        long startTime = System.currentTimeMillis();
        Integer[][] domain = new Integer[k][];

        for (int i = 0; i < k; i++) {
            Integer[] choice = new Integer[n];
            for (int j = 0; j<n; j++) {
                choice[j] = j+1;
                //System.out.print(j + 1 + ", ");
            }
            //System.out.println();
            domain[i] = choice;
        }

        List<Constraint<Integer>> constraints = new ArrayList<>();

        Set<Set<Integer>> known_solutions = new HashSet<>();

        Constraint<Integer> biggerThanPrev = new Constraint<>(xs -> {
            int prev = xs.get(0) - 1;
            for (Integer i : xs) {
                if(i <= prev) {
                    return false;
                }
                prev = i;
            }
            return true;
        }, new int[] {0});
        constraints.add(biggerThanPrev);

        List<List<Integer>> solutions = new ArrayList<>();
        int[] searchFrom = new int[k];
        Integer[][] oldDomain = domain;
        List<Integer> solution = mySolver.backTracking_helper(domain, constraints);
        while (solution != null) {
            List<Constraint<Integer>> new_constraints = new ArrayList<>(constraints);

            List<Integer> finalSolution1 = solution;
            List<Integer> actualSolution = new ArrayList<>();
            for (int i = 0; i < solution.size(); i++) {
                actualSolution.add(oldDomain[i][solution.get(i)]);
            }
            solutions.add(actualSolution);
            known_solutions.add(new HashSet<>(actualSolution));

            //creating new helper domain
            for (int i = 0; i < solution.size(); i++) {
                if(solution.get(i) == n-1 && i != 0) {
                    searchFrom[i-1]++;
                    for (int j = i; j < solution.size(); j++){
                        searchFrom[j] = 0;
                    }
                    break;
                }
                searchFrom[i] = solution.get(i);
            }
            Integer[] test = Arrays.copyOfRange(domain[2],searchFrom[2],n);
            for (int i : test) {
                System.out.print(i + " ");
            }
            System.out.println("");
            Integer[][] helperDomain = new Integer[k][];
            for (int i = 0; i < k; i++) {
                helperDomain[i] = Arrays.copyOfRange(domain[i],searchFrom[i],n);
            }

            // Check solution > prev
            Integer[][] finalOldDomain = oldDomain;
            Constraint<Integer> largerThanConstraint = new Constraint<>(xs -> compareNumbers2(xs, finalSolution1, helperDomain, finalOldDomain), new int[] {0});
            new_constraints.add(largerThanConstraint);
            //Check if solution exists
            Constraint<Integer> noReps = new Constraint<>(xs -> {
                List<Integer> actualList = new ArrayList<>();
                for (int i = 0; i < xs.size(); i++) {
                    actualList.add(helperDomain[i][xs.get(i)]);
                }
                Set<Integer> set = new HashSet<>(actualList);
                if (!known_solutions.contains(set)) {
                    return true;
                }
                return false;
            }, new int[] {0});
            new_constraints.add(noReps);
            solution = mySolver.backTracking_helper(helperDomain, new_constraints);
            oldDomain = helperDomain;
            /*
            1563200
            60075
            26
            19600
             */
        }
        /*
        // Collect the result and convert it to the correct datastructure.
        List<String> finalSolution = all_solutions.stream().map(characters ->
                characters.stream().map(Object::toString).reduce((acc, e) -> acc + e).get()
        ).sorted().collect(Collectors.toList());
         */

        List<int[]> finalSolution = new ArrayList<>();
        for (List<Integer> sol : solutions) {
            int[] convert = new int[sol.size()];
            for (int i = 0; i < sol.size(); i++) {
                convert[i] = sol.get(i);
            }
            finalSolution.add(convert);
        }

        System.out.println("All solutions: ");
        for (int[] i : finalSolution) {
            System.out.print("(");
            for (int j : i) {
                System.out.print(j + ", ");
            }
            System.out.print("), ");
        }


        System.out.println("");
        System.out.println(Solver.num);
        System.out.println(Solver.num2);
        System.out.println(Solver.num/Solver.num2);
        System.out.println(finalSolution.size());

        return finalSolution;
    }

    private static boolean compareNumbers2(List<Integer> inputList, List<Integer> oldList, Integer[][] inputSpace, Integer[][] oldSpace) {
        for (int i = 0; i < inputList.size(); i++) {
            if (inputSpace[i][inputList.get(i)] > oldSpace[i][oldList.get(i)]) {
                return true;
            }
            if (inputSpace[i][inputList.get(i)] < oldSpace[i][oldList.get(i)]) {
                return false;
            }
        }
        return inputList.size() != oldList.size();
    }

    public static void main(String[] args) {
        // exampleProblem(3);

        long startTime = System.currentTimeMillis();
        // getBinaryStrings(20);
        getCombinationsWithoutRepetition(5, 3);
        /*
        1563200
        60075
        26
        19600
         */
        /*
        97850
        7225
        13
        2300
        313ms
         */

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
    public static int num = 0;
    public static int num2 = 0;
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
            num++;
            if (isConsistent(domain, new_solution, constraints)) {
                num2++;
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
