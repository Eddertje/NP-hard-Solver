import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Set<List<Character>> solutions = new HashSet<>();

        Solver<Character> mySolver = new Solver<>();
        Character[][] domain = new Character[][] {new Character[] {'0', '1'}, new Character[] {'0', '1'}, new Character[] {'0', '1'}};
        Function<List<Character>, Boolean>[] constraints = new Function[] {(a) -> solutions.contains(a)};

        List<Character> solution = mySolver.solve(domain, constraints);
        while (solution != null) {
            solutions.add(solution);
            System.out.println("Found a solution: " + solution);

            solution = mySolver.solve(domain, constraints);
        }

        System.out.println("All solutions: " + solutions);
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

    public List<A> solve(A[][] domain, Function<List<A>, Boolean>[] constraints) {
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

    private boolean isValid(List<A> solution, Function<List<A>, Boolean>[] constraints) {
        if (solution == null) {
            return false;
        }
        for (var constraint : constraints) {
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
