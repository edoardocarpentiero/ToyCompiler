#include <stdio.h>
#include <stdbool.h>


int n=0;
 int terzo;
 int i=2;
 int primo=1;
 int secondo=1;

void fibonacci(int n){



if(n==0){
	printf("0");
}else if(n==1){
	printf("1");
}else {
	printf("%d ", primo);
	printf("%d ", secondo);
	i=i+1;

while(i<=n){
terzo=primo+secondo;

primo=secondo;

secondo=terzo;

printf("%d ", terzo);
i=i+1;

}
}

}
int f(){


return 2;
}
int d(){


return 2;
}

int main(){
	

printf("Quanti numeri di Fibonacci vuoi Visualizzare?");
scanf("%d",&n);
fibonacci(n);
printf("%d", f()+d());

return 0;
}