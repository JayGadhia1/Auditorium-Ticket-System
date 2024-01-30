class Node {
    private Seat payload;
    private Node next;
    private Node down;

    public Node(Seat payload) {
        this.payload = payload;
        this.next = null;
        this.down = null;
    }

    public Seat getPayload() {
        return payload;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getDown() {
        return down;
    }

    public void setDown(Node down) {
        this.down = down;
    }
}