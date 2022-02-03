.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	block_0_a : .word 0

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
		li $t0, 1
		beq $t0, 0, L1
		la $a0, block_0_a
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $t0, 3
		sw $t0, 0($a3)
		j L1exit
L1:
L1exit:
		la $a0, global_main_a
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
