.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	runtime_error:
		li $v0, 4
		la $a0, error_run_time
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		addi $sp,$sp,4
		li $t0, 0
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 0
		addi $sp, $sp, -4
		lw $t1 0($sp)
		seq $t1, $t1, $t0
		move $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL1
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL1
	printFalseL1:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL1:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		li $t0, 0
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 1
		addi $sp, $sp, -4
		lw $t1 0($sp)
		seq $t1, $t1, $t0
		move $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL2
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL2
	printFalseL2:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL2:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		li $t0, 1
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 0
		addi $sp, $sp, -4
		lw $t1 0($sp)
		seq $t1, $t1, $t0
		move $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL3
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL3
	printFalseL3:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL3:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		li $t0, 1
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 1
		addi $sp, $sp, -4
		lw $t1 0($sp)
		seq $t1, $t1, $t0
		move $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL4
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL4
	printFalseL4:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL4:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra