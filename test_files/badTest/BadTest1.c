#include <stdio.h>
#include <stdbool.h>


float primo,secondo,tot;

int divisione(){


printf("Inserire il dividendo:\n");
scanf("%f",&primo);
printf("Inserire il divisore:\n");
scanf("%f",&secondo);
tot=primo/secondo;


return tot;
}

int main(){
	
int a=0;

printf("%d", divisione());

return 0;
}