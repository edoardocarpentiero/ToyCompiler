#include <stdio.h>
#include <stdbool.h>




void printPeg(int peg){

char * a,b;


if(peg==1){
	printf("left");
}else if(peg==2){
	printf("center");
}else {
	printf("right");
}

}
void hanoi(int n,int toPeg,float fromPeg,float usingPeg){



if(n!=0){
	hanoi(n-1,fromPeg,toPeg,usingPeg);
	printf("Move disk from ");
	printPeg(fromPeg);
	printf(" peg to ");
	printPeg(toPeg);
	printf(" peg.\n");
	hanoi(n-1,usingPeg,fromPeg,toPeg);
}

}

int main(){
	
int n=0;

printf("How many pegs? ");
scanf("%d",&n);
hanoi(n,1,2,3);

return 0;
}