import java.util.LinkedList;

public class LinkedListTranspositionTable implements ConnectFourAI.ITranspositionTable{
    private static class EntryNode {
        long key;
        ConnectFourAI.TableEntry value;
        EntryNode(long key, ConnectFourAI.TableEntry value) {
            this.key = key;
            this.value = value;
        }
    }
    private final LinkedList<EntryNode> list;

    public LinkedListTranspositionTable() {
        this.list = new LinkedList<>();
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
                node.value = entryValue; // Update existing
                return;
            }
        }
        list.add(new EntryNode(key, entryValue)); // Add to the end
    }

    @Override
    public void clear() {
        list.clear();
    }
}
