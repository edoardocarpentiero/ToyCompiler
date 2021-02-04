#include <stdio.h>
#include <stdbool.h>


float primo,secondo,tot=0;
 int i=0;

float moltiplica(){


printf("Inserire il primo argomento:\n");
scanf("%f",&primo);
printf("Inserire il secondo argomento:\n");
scanf("%f",&secondo);
i=i+1;

while(i<=secondo){
tot=tot+primo;

i=i+1;

}

return tot;
}

int main(){
	

printf("%f", moltiplica());
printf("\n");

return 0;
}