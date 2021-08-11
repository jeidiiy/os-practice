#include <stdio.h>
#include <unistd.h>

int main()
{
  int i;
  pid_t pid;

  for (i = 0; i < 4; i++)
  {
    pid = fork();
    printf("Hello, Fork() %d %d\n", pid, i);
  }

  return 0;
}