#include <stdio.h>
#include <stdbool.h>


int primo,secondo,tot;

int divisione(){


printf("Inserire il dividendo:\n");
scanf("%d",&primo);
printf("Inserire il divisore:\n");
scanf("%d",&secondo);
tot=primo/secondo;


return tot;
}

int main(){
	
int a=0;

printf("%d", divisione());

return 0;
}