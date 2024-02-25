import {Button, Card, CardBody, CardHeader, CardText, CardTitle, Col, Container, Row} from "react-bootstrap";
import {RxReload} from "react-icons/rx";
import {useMutation, useQueryClient} from "react-query";
import ApplicationService from "../../../../services/ApplicationService";
import DateHelper from "../../../../helpers/DateHelper";
import styles from "../application.module.css";
import('../../../../styles/applicationItem.css');


export const ApplicationItem = ({id, dublicate_end, build, room, name, email, start, end, dublicate}) => {

    let endDate
    if (dublicate) {
        endDate = DateHelper.normalizeDate(dublicate_end)
    }

    const queryClient = useQueryClient()
    const acceptMutation = useMutation((id) => ApplicationService.acceptApplication(id), {
        onSuccess() {
            queryClient.invalidateQueries(['applications'])
        }
    })

    const declineMutation = useMutation((id) => ApplicationService.declineApplication(id), {
        onSuccess() {
            queryClient.invalidateQueries(['applications'])
        }
    })

    return (
        <>
            <Container className='mt-3'>
                <Card>
                    <CardHeader className='container-fluid '>
                        <Row className='w-100'>
                            <Col className={'d-flex gap-3'}>
                                <div className='d-flex gap-3'>
                                    <CardTitle>Корпус <span className='fw-bold'>{build}</span></CardTitle>
                                    <CardTitle className="border-end border-black pe-3">Аудитория <span
                                        className='fw-bold'>{room}</span></CardTitle>
                                </div>
                                <CardTitle className={!dublicate ?'fw-bold border-end border-black pe-3' : 'fw-bold'}>{start.day}.{start.month}.{start.year}</CardTitle>
                                {dublicate && <CardTitle>-</CardTitle>}
                                {dublicate &&
                                    <CardTitle
                                        className={dublicate ? 'fw-bold border-end border-black pe-3' : 'fw-bold'}>{endDate.day}.{endDate.month}.{endDate.year}</CardTitle>}
                                <CardTitle
                                    className='fw-bold'>{start.hours}:{start.minutes} - {end.hours}:{end.minutes}</CardTitle>
                                {dublicate &&
                                    <>
                                        <RxReload title={'Повторяющаяся заявка'}/>
                                    </>
                                }

                            </Col>
                        </Row>
                    </CardHeader>
                    <CardBody>
                        <Row className='w-100 d-flex align-items-center'>

                            <Col xs={12} md={8} lg={8} className={'d-flex card-text gap-3 align-items-end'}>
                                <span className='text-muted'>ФИО: <span
                                    className={'fs-5 text-black'}>{name}</span> </span>
                                <span className='text-muted'>Email: <span
                                    className={'fs-5 text-black'}>{email}</span> </span>
                            </Col>

                            <Col xs={12} md={1} lg={4}
                                 className={` ${styles.buttons} ms-auto d-flex gap-3 mt-3 mt-sm-0 justify-content-end`}>
                                <Button onClick={() => acceptMutation.mutate(id)}
                                        className='get border-0 rounded-4'>Одобрить</Button>
                                <Button onClick={() => declineMutation.mutate(id)}
                                        className='delit border-0 rounded-4'>Отклонить</Button>
                            </Col>
                        </Row>

                    </CardBody>
                </Card>
            </Container>

        </>
    )
}