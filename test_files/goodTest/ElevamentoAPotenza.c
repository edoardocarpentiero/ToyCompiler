#include <stdio.h>
#include <stdbool.h>

typedef struct{
float variable0;
float variable1;
}typemoltiplica;
float primo,secondo,tot=0,tot2=0;
 int i=0;

typemoltiplica moltiplica(){
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
returnVal.variable1=tot2;
return returnVal;
}

int main(){
	typemoltiplica returnVal1 = moltiplica();

printf("Moltiplicazione ed Elevamento a Potenza: %f%f", returnVal1.variable0,returnVal1.variable1);
printf("\n");

return 0;
}