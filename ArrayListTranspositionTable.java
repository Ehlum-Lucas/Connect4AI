import java.util.ArrayList;

public class ArrayListTranspositionTable implements ConnectFourAI.ITranspositionTable {
    private static class EntryNode {
        long key;
        ConnectFourAI.TableEntry value;
        EntryNode(long key, ConnectFourAI.TableEntry value) {
            this.key = key;
            this.value = value;
        }
    }

    private final ArrayList<EntryNode> list;

    public ArrayListTranspositionTable() {
        this.list = new ArrayList<>();
    }

    @Override
    public ConnectFourAI.TableEntry get(long key) {
        for (EntryNode node : list) {
            if (node.key == key) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public void put(long key, ConnectFourAI.TableEntry entryValue) {
        for (EntryNode node : list) {
            if (node.key == key) {
                node.value = entryValue;
                return;
            }
        }
        list.add(new EntryNode(key, entryValue));
    }

    @Override
    public void clear() {
        list.clear();
    }
}
