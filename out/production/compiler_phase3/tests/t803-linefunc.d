void line__func__line() {
    Print("Strange function name: ", __func__, ", line: ", __line__);
}
int main(){
    line__func__line();
}