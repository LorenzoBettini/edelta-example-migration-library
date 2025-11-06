# Edelta Performance Results

30 October 2025.

Some performance results in several machines (including GitHub Actions virtual environments), executed by the Java file `edelta.example.migration.library.performance.EdeltaLibraryModelMigratorPerformanceStatistics`.

## Dell Pro Max Tower T2

```text
Measuring impact of number of files (constant size per file)...
  files=4 -> 18.38 ms
  files=8 -> 22.03 ms
  files=16 -> 35.35 ms
  files=32 -> 43.02 ms
  files=64 -> 62.73 ms
  files=128 -> 82.40 ms
  files=256 -> 117.72 ms
  files=512 -> 216.31 ms
  files=1024 -> 350.00 ms

Measuring impact of model size (constant number of files = 4)...
  factor=8, totalElements=64 -> 4.08 ms
  factor=16, totalElements=128 -> 4.69 ms
  factor=32, totalElements=256 -> 17.89 ms
  factor=64, totalElements=512 -> 13.35 ms
  factor=128, totalElements=1024 -> 18.77 ms
  factor=256, totalElements=2048 -> 30.84 ms
  factor=512, totalElements=4096 -> 52.20 ms
  factor=1024, totalElements=8192 -> 102.38 ms
  factor=2048, totalElements=16384 -> 171.42 ms
```

## LG GRAM

```text
Measuring impact of number of files (constant size per file)...
  files=4 -> 47.59 ms
  files=8 -> 76.42 ms
  files=16 -> 145.21 ms
  files=32 -> 152.19 ms
  files=64 -> 330.70 ms
  files=128 -> 567.10 ms
  files=256 -> 739.91 ms
  files=512 -> 877.38 ms
  files=1024 -> 1.589 s

Measuring impact of model size (constant number of files = 4)...
  factor=8, totalElements=64 -> 23.02 ms
  factor=16, totalElements=128 -> 31.91 ms
  factor=32, totalElements=256 -> 49.83 ms
  factor=64, totalElements=512 -> 105.18 ms
  factor=128, totalElements=1024 -> 141.59 ms
  factor=256, totalElements=2048 -> 263.25 ms
  factor=512, totalElements=4096 -> 458.94 ms
  factor=1024, totalElements=8192 -> 613.30 ms
  factor=2048, totalElements=16384 -> 887.96 ms
```

## GitHub Actions

### Linux Runner

Ubuntu 24.04.3 LTS

```text
Measuring impact of number of files (constant size per file)...
  files=4 -> 43.51 ms
  files=8 -> 62.67 ms
  files=16 -> 95.25 ms
  files=32 -> 145.31 ms
  files=64 -> 220.82 ms
  files=128 -> 326.23 ms
  files=256 -> 576.89 ms
  files=512 -> 1.075 s
  files=1024 -> 2.094 s

Measuring impact of model size (constant number of files = 4)...
  factor=8, totalElements=64 -> 17.02 ms
  factor=16, totalElements=128 -> 20.55 ms
  factor=32, totalElements=256 -> 36.00 ms
  factor=64, totalElements=512 -> 66.83 ms
  factor=128, totalElements=1024 -> 99.86 ms
  factor=256, totalElements=2048 -> 179.00 ms
  factor=512, totalElements=4096 -> 312.90 ms
  factor=1024, totalElements=8192 -> 552.85 ms
  factor=2048, totalElements=16384 -> 953.55 ms
```

### macOS Runner

macOS 15.7.1 arm64

```text
Measuring impact of number of files (constant size per file)...
  files=4 -> 49.10 ms
  files=8 -> 40.83 ms
  files=16 -> 70.54 ms
  files=32 -> 150.66 ms
  files=64 -> 203.54 ms
  files=128 -> 273.81 ms
  files=256 -> 751.16 ms
  files=512 -> 959.90 ms
  files=1024 -> 1.774 s

Measuring impact of model size (constant number of files = 4)...
  factor=8, totalElements=64 -> 14.04 ms
  factor=16, totalElements=128 -> 12.11 ms
  factor=32, totalElements=256 -> 40.65 ms
  factor=64, totalElements=512 ->  91.58 ms
  factor=128, totalElements=1024 -> 130.56 ms
  factor=256, totalElements=2048 -> 132.63 ms
  factor=512, totalElements=4096 -> 175.42 ms
  factor=1024, totalElements=8192 -> 274.66 ms
  factor=2048, totalElements=16384 -> 610.20 ms
```

### Windows Runner

Microsoft Windows Server 2025

```text
Measuring impact of number of files (constant size per file)...
  files=4 -> 53.30 ms
  files=8 -> 83.48 ms
  files=16 -> 132.93 ms
  files=32 -> 222.68 ms
  files=64 -> 345.47 ms
  files=128 -> 565.46 ms
  files=256 -> 1.028 s
  files=512 -> 1.949 s
  files=1024 -> 3.664 s

Measuring impact of model size (constant number of files = 4)...
  factor=8, totalElements=64 -> 24.28 ms
  factor=16, totalElements=128 -> 30.65 ms
  factor=32, totalElements=256 -> 49.76 ms
  factor=64, totalElements=512 -> 81.78 ms
  factor=128, totalElements=1024 -> 138.76 ms
  factor=256, totalElements=2048 -> 219.58 ms
  factor=512, totalElements=4096 -> 341.79 ms
  factor=1024, totalElements=8192 -> 490.89 ms
  factor=2048, totalElements=16384 -> 1.031 s
```
