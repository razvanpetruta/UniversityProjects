#ifndef COMMON_H_
#define COMMON_H_

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>

void writeToFD(int fd, char* buf)
{
    int len = strlen(buf);
    write(fd, &len, sizeof(int));
    write(fd, buf, len * sizeof(char));
}

void readFromFD(int fd, int max, char* buf)
{
    int readBytes = 0;
    while(readBytes < max)
    {
        int k;
        if((k = read(fd, buf + readBytes, (max - readBytes) * sizeof(char))) > 0)
            readBytes += k;
    }
}

#endif