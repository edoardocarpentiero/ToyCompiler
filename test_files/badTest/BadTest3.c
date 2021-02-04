#include <stdio.h>
#include <stdbool.h>

typedef struct{
float variable0;
float variable1;
}typemoltiplica;
typedef struct{
float variable0;
float variable1;
}typef;
float primo,secondo,tot=0,tot2=0;
 int i;

typemoltiplica moltiplica(int a,float asd){
typemoltiplica returnVal;


printf("Inserire il primo argomento:\n");
scanf("%f",&primo);
printf("Inserire il secondo argomento:\n");
scanf("%f",&secondo);
tot=primo;

tot2=primo;

i=i+1;

while(i<secondo){
tot=tot+primo;

tot2=tot2*primo;

i=i+1;

}

returnVal.variable0=tot;
returnVal.variable1=2;
return returnVal;
}
typef f(){
typef returnVal;


returnVal.variable0=2;
returnVal.variable1=3;
return returnVal;
}

int main(){
	typef returnVal1 = f();
typef returnVal2 = moltiplica(returnVal1.variable0,returnVal1.variable1);
typef returnVal3 = moltiplica(returnVal2.variable0,returnVal2.variable1);
int a,b;

printf("ciao%f%f", returnVal3.variable0,returnVal3.variable1);

return 0;
}