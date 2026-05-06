import java.util.List;

public interface IAttackService {
    List<String> calculateAttack(String pole, int n, List<String> przeszkody);
    int count(List<String> atakowanePola);
}