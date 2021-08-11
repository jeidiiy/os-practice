#include <stdio.h>
#include <unistd.h>

int main()
{
  fork();
  fork();
  fork();
  printf("Hello, Fork()\n");

  return 0;
}