#include <stdio.h>
#include <stdbool.h>


int n=0;

int factorial(int n){

int result=0;


if(n==0){
	result=1;

}else {
	result=n*factorial(n-1);;

}

return result;
}

int main(){
	

printf("Enter n, or >= 10 to exit: ");
scanf("%d",&n);
while(n<10){
printf("Enter n, or >= 10 to exit: ");
scanf("%d",&n);
printf("Factorial of %d is %d\n", n,factorial(n));
}

return 0;
}