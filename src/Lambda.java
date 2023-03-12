import java.util.ArrayList;
import java.util.List;

public interface Lambda<A> {

    boolean evalPost(List<A> a);
}
