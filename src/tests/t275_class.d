class c{
    public int number;
    public c f(int x){
        c h;
        h=new c;
        h.number=x;
        return h;
    }
    public void d(){
        Print(number);
    }
}
int main() {
    c a;
    a=new c;
    a.number = 10;
    a.f(10).f(15).f(32).f(64).d();
}
