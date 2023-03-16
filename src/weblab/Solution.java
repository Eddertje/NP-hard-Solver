package weblab;

import java.util.*;
import java.util.function.Function;

class Solution {
    public static Assignment exampleProblem(int n) {

        //Binary Strings
        Solver<Integer> mySolver = new Solver<>();
        Integer[][] domain = new Integer[n][];
        for (int i = 0; i < n; i++) {
            domain[i] = new Integer[] {0, 1};
        }

        List<Constraint<Integer>> constraints = new ArrayList<>();
        constraints.add(new Constraint<>((a) -> a.get(0) - a.get(2) >= 0));
        constraints.add(new Constraint<>((a) -> a.get(1) != 0));

        Assignment solution = mySolver.solve(domain, constraints);

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
    public Assignment solve(A[][] domain, List<Constraint<A>> constraints) {
        Assignment solution = createAssignment(domain);
        int index = 0;
        do {
            if (index > 100) return null;
            index++;

            System.out.println("Looking at solution: " + solution);
            if (isValid(getSolution(domain, solution.solution), constraints)) {
                return solution;
            }
            solution = createNextAssignment(domain, solution);
        } while (solution != null);
        return null;
    }

    private boolean isValid(List<A> assignment, List<Constraint<A>> constraints) {
        for (Constraint<A> constraint : constraints) {
            if (!constraint.eval.apply(assignment)) {
                return false;
            }
        }
        return true;
    }

    private Assignment createAssignment(A[][] variables) {
        Assignment assignment = new Assignment();
        for (int i = 0; i < variables.length; i++) {
            assignment.solution.add(0);
        }
        return assignment;
    }

    private Assignment createNextAssignment(A[][] variables, Assignment assignment) {
        for (int i = assignment.solution.size()-1; i >= 0; i--) {
            if (assignment.solution.get(i) < variables[i].length-1) {
                assignment.solution.set(i, assignment.solution.get(i) + 1);
                return assignment;
            }
        }
        return null;
    }

    private List<A> getSolution(A[][] domain, List<Integer> variables) {
        List<A> solution = new ArrayList<>();
        for (int i = 0; i < domain.length; i++) {
            solution.add(domain[i][variables.get(i)]);
        }
        return solution;
    }
}

class Constraint<A> {
    public Function<List<A>, Boolean> eval;
    public Constraint(Function<List<A>, Boolean> eval) {
        this.eval = eval;
    }
}

class Assignment {
    public List<Integer> solution;

    public Assignment() {
        this.solution = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.solution.toString();
    }
}
