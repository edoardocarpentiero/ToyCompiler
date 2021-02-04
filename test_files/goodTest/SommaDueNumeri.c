#include <stdio.h>
#include <stdbool.h>


int primo,secondo,tot;

int somma(){


printf("Inserire il primo argomento:\n");
scanf("%d",&primo);
printf("Inserire il secondo argomento:\n");
scanf("%d",&secondo);
tot=primo+secondo;


return tot;
}

int main(){
	
int a=0;

printf("\n%d", somma());
printf("\n");

return 0;
}