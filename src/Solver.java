import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver<A> {
    public List<A> solve(ArrayList<A[]> domain, ArrayList<Constraint<A>> constraints) {
        boolean change;
        do {
            change = false;
            for (Constraint constraint : constraints) {
                if(constraint.prePost) {
                    continue;
                }
                ArrayList<A[]> vars = new ArrayList<>();
                for (int i : constraint.variables) {
                    vars.add(domain.get(i));
                }
                change |= false;//constraint.lambda.eval(vars);
                if(change == false) {
                    break;
                }
            }
        } while (change);

        List<List<A>> variations = getAllCombinations(domain);

        for (List<A> variation: variations) {
            boolean isSolution = true;
            for (Constraint constraint : constraints) {
                if(!constraint.prePost) {
                    continue;
                }
                boolean valid = constraint.lambda.evalPost(variation);
                if(!valid) {
                    isSolution = false;
                    break;
                }
            }
            if(isSolution) {
                return variation;
            }
        }
        return null;
    }

    public static <A> List<List<A>> getAllCombinations(ArrayList<A[]> domain) {
        List<List<A>> result = new ArrayList<>();
        List<A> currentCombination = new ArrayList<>();
        getAllCombinationsHelper(domain, result, currentCombination, 0);
        return result;
    }

    private static <A> void getAllCombinationsHelper(ArrayList<A[]> domain, List<List<A>> result,
                                                     List<A> currentCombination, int domainIndex) {
        if (domainIndex == domain.size()) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        A[] options = domain.get(domainIndex);
        for (int i = 0; i < options.length; i++) {
            currentCombination.add(options[i]);
            getAllCombinationsHelper(domain, result, currentCombination, domainIndex + 1);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

}
