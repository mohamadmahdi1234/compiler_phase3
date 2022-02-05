
class A {
    public void a(Z z) {
        Print("entering method a of A");
        b(z);
        Print("exiting method a of A");
    }

    private void b(Z z) {
        Print("entering method b of A");
        z.c();
        Print("exiting method b of A");
    }
}

class Z {
    public void c() {
        Print("inside method c of Z");
    }
}

int main() {
    A a;
    Z z;
    a = new A;
    z = new Z;

    a.a(z);
}
