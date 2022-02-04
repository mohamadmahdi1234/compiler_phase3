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
		jal global_test
		addi $sp, $sp, 0
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
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	global_test:
		sw $ra,0($sp)
		addi $sp,$sp,4
		li $t0, 0
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
