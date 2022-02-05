.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_p :	.space	8
	global_main_name : .word 0
	global_main_age : .word 0
	userInput_L1:	.space	600
	Person_name : .word 0
	Person_age : .word 0
	Person_setName_new_name : .word 0
	Person_setAge_new_age : .word 0
	StringLiteral_01: .asciiz "Name: "
	StringLiteral_11: .asciiz " Age: "

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
		la $a0, global_main_name
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 8
		la $a0, userInput_L1
		li $a1, 600
		syscall
		move $t0, $a0

		sw $t0, 0($a3)
		la $a0, global_main_age
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 5
		syscall
		move $t0, $v0

		sw $t0, 0($a3)
		la $a3, 0($a0) 
		la $a0, global_main_name
		lw $t0, 0($a0)
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal Person_setName
		addi $sp, $sp, -4
		la $a0, global_main_age
		lw $t0, 0($a0)
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal Person_setAge
		addi $sp, $sp, -4
		jal Person_print
		addi $sp, $sp, 0
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	Person_setName:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, Person_setName_new_name
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $a0, Person_name
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $a0, Person_setName_new_name
		lw $t0, 0($a0)
		sw $t0, 0($a3)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	Person_setAge:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, Person_setAge_new_age
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $a0, Person_age
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $a0, Person_setAge_new_age
		lw $t0, 0($a0)
		sw $t0, 0($a3)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	Person_print:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $a0, Person_name
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $a0, Person_age
		lw $t0, 0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
