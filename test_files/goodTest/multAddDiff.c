#include <stdio.h>
#include <stdbool.h>

typedef struct{
int variable0;
int variable1;
int variable2;
}typemultAddDiff;
char * nome="Michele";
 int a;


typemultAddDiff multAddDiff(){
typemultAddDiff returnVal;

int primo,secondo,mul,add,diff;

printf("Inserire il primo argomento:\n");
scanf("%d",&primo);
printf("Inserire il secondo argomento:\n");
scanf("%d",&secondo);
mul=primo*secondo;
add=secondo+primo;
diff=primo-primo;


returnVal.variable0=mul;
returnVal.variable1=add;
returnVal.variable2=diff;
return returnVal;
}
void writeNewLines(int n){


while(n>0){
printf("\n");
n=n-1;

}

}
int main(){
	typemultAddDiff returnVal1 = multAddDiff();
int a,b,c=0;

a = returnVal1.variable0;
b = returnVal1.variable1;
c = returnVal1.variable2;

printf("Ciao%s", nome);
writeNewLines(2);
printf("I tuoi valori sono:\n%d%d%d", a,b,c);

return 0;
}