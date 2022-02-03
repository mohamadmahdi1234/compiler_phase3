.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_a : .word 0
	global_main_b : .word 0
	global_test_a : .word 0
	global_test_b : .word 0

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
		la $a0, global_main_a
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 5
		syscall
		move $t0, $v0

		sw $t0, 0($a3)
		la $a0, global_main_b
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 5
		syscall
		move $t0, $v0

		sw $t0, 0($a3)
		la $a0, global_main_a
		lw $t0, 0($a0)
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		la $a0, global_main_b
		lw $t0, 0($a0)
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal global_test
		addi $sp, $sp, -8
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
	global_test:
		sw $ra,0($sp)
		addi $sp,$sp,-8
		la $a1, global_test_a
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a1, global_test_b
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $a0, global_test_a
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		la $a0, global_test_b
		lw $t0, 0($a0)
		addi $sp, $sp, -4
		lw $t1 0($sp)
		mul $t1, $t1, $t0
		move $t0, $t1
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
