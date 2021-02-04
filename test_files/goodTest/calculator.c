#include <stdio.h>
#include <stdbool.h>


float primo,secondo,tot;
 int op;

float calc(){


printf("Inserire il primo argomento:\n");
scanf("%f",&primo);
printf("Inserire il secondo argomento:\n");
scanf("%f",&secondo);
printf("Inserire operatore:\n");
printf("1 per Addizione \n");
printf("2 per Sottrazione \n");
printf("3 per Moltiplicazione \n");
printf("4 per Divisione \n");
scanf("%d",&op);

if(op==1){
	tot=primo+secondo;

}else if(op==4){
	tot=primo/secondo;

}
	else if(op==3){
	tot=primo*secondo;

}
	else if(op==2){
	tot=primo-secondo;

}else {
	printf("errore\n");
}

return tot;
}

int main(){
	
float a=calc();

printf("il risultato e' %f", a);

return 0;
}